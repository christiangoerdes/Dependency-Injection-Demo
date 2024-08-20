package org.example.framework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    static class Database {
        Database() {
        }
    }

    static class UserRepository {
        private final Database database;

        UserRepository(Database database) {
            this.database = database;
        }
    }

    static class UserService {
        private final UserRepository userRepository;

        UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }
    }

    static class Client {
        private final UserService userService;

        Client(UserService userService) {
            this.userService = userService;
        }
    }

    static class A {
        private final B b;

        A(B b) {
            this.b = b;
        }
    }

    static class B {
        private final A a;

        B(A a) {
            this.a = a;
        }
    }

    @Test
    void testSimpleDependencyResolution() {
        Injector injector = new Injector();
        Database db = injector.resolve(Database.class);
        assertNotNull(db);
    }

    @Test
    void testComplexDependencyResolution() {
        Injector injector = new Injector();
        Client client = injector.resolve(Client.class);
        assertNotNull(client);
    }

    @Test
    void testSingletonBehavior() {
        Injector injector = new Injector();
        UserRepository repo1 = injector.resolve(UserRepository.class);
        UserRepository repo2 = injector.resolve(UserRepository.class);
        assertSame(repo1, repo2);
    }

    @Test
    void testCircularDependencyDetection() {
        Injector injector = new Injector();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            injector.resolve(A.class);
        });

        // Check the complete exception chain for the circular dependency message
        Throwable cause = exception;
        boolean circularDependencyDetected = false;

        while (cause != null) {
            if (cause.getMessage().contains("Circular dependency detected")) {
                circularDependencyDetected = true;
                break;
            }
            cause = cause.getCause();
        }

        assertTrue(circularDependencyDetected, "Circular dependency exception should be detected in the exception chain.");
    }
}
