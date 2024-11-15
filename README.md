## Облачное хранилище

### Описание проекта

Проект представляет из себя REST-сервис для загрузки файлов и вывода списка уже загруженных
файлов пользователя.

Заранее подготовленное веб-приложение (FRONT) подключается к разработанному сервису через
endpoint вида [http://backend:8080](http://backend:8080).

API методы описаны в спецификации [CloudServiceSpecification.yaml](https://github.com/netology-code/jd-homeworks/blob/master/diploma/CloudServiceSpecification.yaml)

#### Схема работы полного приложения
![Схема](https://github.com/user-attachments/assets/b586f89a-d790-43d5-8484-da8e648f05c2)
## Приложение “Облачное хранилище”

- Приложение разработано с использованием Spring Boot
- Использован сборщик Maven
Данное приложение позволяет пользователям работать с файлами:
- загружать файлы
- получать файлы по имени
- переименовывать файлы
- удалять файлы
- просматривать список файлов

Имя БД, логин и пароль хранятся в .env
