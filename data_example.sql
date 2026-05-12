INSERT INTO Teachers (last_name, first_name, patronymic, specialization, hire_date, hourly_rate, experience)
VALUES ('Иванов', 'Петр', 'Сергеевич', 'Программирование Java', '2025-07-05', 6.00, 2),
       ('Моисеенко', 'Татьяна', 'Антоновна', 'SQL-разработчик', '2025-09-12', 5.00, 1),
       ('Вивьенов', 'Михаил', 'Петрович', 'Мобильные приложения', '2025-02-06', 7.00, 3);

INSERT INTO Courses (course_name, teacher_id, start_date, course_cost, total_hours, description)
VALUES ('Основы разработки на Java', 1, '2026-05-05', 1000, 40, 'Синтаксис Java, ООП, коллекции, основы многопоточности.'),
       ('Продвинутая Java', 1, '2026-05-25', 1500, 70, 'Stream API, generics, reflection, работа с сетью и IO.'),
       ('Базы данных', 2, '2026-06-01', 700, 30, 'Проектирование схем, SQL, нормализация, индексы.');
