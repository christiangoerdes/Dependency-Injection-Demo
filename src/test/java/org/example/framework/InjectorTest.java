package org.example.framework;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    @Nested
    class SimpleDependencyTests {
        interface SimpleService {
            void execute();
        }

        static class SimpleServiceImpl implements SimpleService {
            public void execute() {
                System.out.println("SimpleServiceImpl executing");
            }
        }

        static class SimpleClient {
            private final SimpleService service;

            SimpleClient(SimpleService service) {
                this.service = service;
            }

            void doWork() {
                service.execute();
            }
        }

        @Test
        void testSimpleDependencyInjection() {
            Injector injector = new Injector();
            injector.registerImplementation(SimpleService.class, SimpleServiceImpl.class);

            SimpleClient client = injector.resolve(SimpleClient.class);
            assertNotNull(client);
            assertInstanceOf(SimpleServiceImpl.class, client.service);
        }
    }

    @Nested
    class ComplexDependencyTests {
        interface Repository {
            void save();
        }

        static class RepositoryImpl implements Repository {
            public void save() {
                System.out.println("RepositoryImpl saving data");
            }
        }

        interface Service {
            void perform();
        }

        static class ServiceImpl implements Service {
            private final Repository repository;

            ServiceImpl(Repository repository) {
                this.repository = repository;
            }

            public void perform() {
                repository.save();
            }
        }

        static class ComplexClient {
            private final Service service;

            ComplexClient(Service service) {
                this.service = service;
            }

            void execute() {
                service.perform();
            }
        }

        @Test
        void testComplexDependencyInjection() {
            Injector injector = new Injector();
            injector.registerImplementation(Repository.class, RepositoryImpl.class);
            injector.registerImplementation(Service.class, ServiceImpl.class);

            ComplexClient client = injector.resolve(ComplexClient.class);
            assertNotNull(client);
            assertInstanceOf(ServiceImpl.class, client.service);
        }
    }

    @Nested
    class CircularDependencyTests {
        static class A {A(B b) {}}
        static class B {B(A a) {}}

        @Test
        void testCircularDependencyDetection() {
            Injector injector = new Injector();

            Throwable cause = assertThrows(RuntimeException.class, () -> injector.resolve(A.class));
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
}
