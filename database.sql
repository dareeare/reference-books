-- Создание базы данных
CREATE DATABASE EducationalCenter;
\c EducationalCenter;

-- Таблица справочника "Преподаватели" (с разделением на Фамилию, Имя, Отчество)
CREATE TABLE Teachers (
    teacher_id SERIAL PRIMARY KEY,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    patronymic VARCHAR(100),
    specialization VARCHAR(150) NOT NULL,
    hire_date DATE NOT NULL,
    hourly_rate DECIMAL(10, 2) CHECK (hourly_rate >= 0),
    experience INTEGER DEFAULT 0 CHECK (experience >= 0)
);

-- Таблица справочника "Курсы обучения"
CREATE TABLE Courses (
    course_id SERIAL PRIMARY KEY,
    course_name VARCHAR(200) NOT NULL,
    teacher_id INTEGER,
    start_date DATE,
    course_cost DECIMAL(10, 2) CHECK (course_cost >= 0),
    total_hours INTEGER CHECK (total_hours > 0),
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_courses_teacher FOREIGN KEY (teacher_id) 
        REFERENCES Teachers(teacher_id) ON DELETE SET NULL
);

-- Индексы для оптимизации
CREATE INDEX idx_courses_teacher ON Courses(teacher_id);
CREATE INDEX idx_teachers_last_name ON Teachers(last_name);
CREATE INDEX idx_teachers_specialization ON Teachers(specialization);

-- Комментарии
COMMENT ON TABLE Teachers IS 'Справочник преподавателей';
COMMENT ON TABLE Courses IS 'Справочник курсов обучения';
COMMENT ON COLUMN Teachers.last_name IS 'Фамилия преподавателя';
COMMENT ON COLUMN Teachers.first_name IS 'Имя преподавателя';
COMMENT ON COLUMN Teachers.patronymic IS 'Отчество преподавателя';
COMMENT ON COLUMN Courses.is_deleted IS 'Флаг мягкого удаления';
