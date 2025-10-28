## Модуль 5. JMS: RabbitMq, Kafka, ActiveMQ

---

### Задача:
Реализовать микросервис (*notification-service*) для отправки сообщения на почту при удалении или добавлении пользователя.

### Требования:
* Использовать необходимые модули spring и kafka.
* При удалении или создании юзера приложение, реализованное до этого (*user-service*), должно отправлять сообщение в kafka,
  в котором содержится информация об операции (`удаление` или `создание`) и email юзера.
* Новый микросервис (*notification-service*) должен получить сообщение из kafka и отправить сообщение на почту юзера в
  зависимости от операции:
    * *Удаление - Здравствуйте! Ваш аккаунт был удалён.*
    * *Создание - Здравствуйте! Ваш аккаунт на сайте `ваш сайт` был успешно создан.*
* Также отдельно добавить API, которая будет отправлять сообщение на почту (почти тот же функционал, что и через кафку).
* Написать интеграционные тесты для проверки отправки сообщения на почту.

---
### Описание:
* Директория [user-service](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/tree/main/src/main/java/com/akulov/springboot/userservice_withkafka)
* Прямые ссылки:
    * [entity](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/entity/User.java)
    * [mappings](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/utils/MappingUtils.java)
    * [user_dto](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/dto/UserDto.java)
    * [notification_dto](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/dto/NotificationDto.java)
    * [enums](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/enums/OperationType.java)
    * [repository](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/repository/UserRepository.java)
    * [user_service_impl](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/service/UserServiceImpl.java)
    * [controller](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/controller/UserController.java)
    * [kafka_producer_service](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/service/KafkaProducerService.java)
    * [kafka_producer_config](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/configuration/KafkaProducerConfig.java)
    * [точка входа](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/java/com/akulov/springboot/userservice_withkafka/UserServiceWithKafkaApplication.java)
    * [properties](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/main/resources/application.properties)
    * тесты:
      * [UserControllerTest](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/test/java/com/akulov/springboot/userservice_withkafka/controller/UserControllerTest.java) - unit-тесты ручек контроллера
      * [UserControllerIntegrationTest](https://github.com/MikhailAkulov/springboot_user-service_with_kafka/blob/main/src/test/java/com/akulov/springboot/userservice_withkafka/integration/UserControllerIntegrationTest.java) - проверка отправки уведомлений Kafka
* Директория [notification-service](https://github.com/MikhailAkulov/springboot_kafka_notification-service/tree/main/src/main/java/com/akulov/springboot/notificationservice)
* Прямые ссылки:
    * [kafka_consumer_config](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/configuration/KafkaConsumerConfig.java)
    * [kafka_notification_consumer](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/consumer/KafkaNotificationConsumer.java)
    * [kafka_controller](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/controller/KafkaController.java)
    * [email_request_dto](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/dto/EmailRequestDto.java)
    * [notification_dto](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/dto/NotificationDto.java)
    * [enums](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/enums/OperationType.java)
    * [email_service](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/service/EmailService.java)
    * [точка входа](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/java/com/akulov/springboot/notificationservice/NotificationServiceApplication.java)
    * [интеграционные_тесты_контроллера](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/test/java/com/akulov/springboot/notificationservice/controller/KafkaControllerIntegrationTest.java)
    * [интергационные_тесты_отправки_уведомлений](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/test/java/com/akulov/springboot/notificationservice/integration/NotificationServiceIntegrationTest.java)
    * [properties](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/main/resources/application.properties)
    * тесты:
      * [KafkaControllerIntegrationTest](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/test/java/com/akulov/springboot/notificationservice/controller/KafkaControllerIntegrationTest.java)
      * [NotificationServiceIntegrationTest](https://github.com/MikhailAkulov/springboot_kafka_notification-service/blob/main/src/test/java/com/akulov/springboot/notificationservice/integration/NotificationServiceIntegrationTest.java)

