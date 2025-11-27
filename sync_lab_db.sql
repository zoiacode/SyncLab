--
-- PostgreSQL database dump
--

\restrict RbCuI3hHQhBeZrFEt56dl2MOLFJeJ4VKIqW7B2yGFGHkbP6u6mT30o6NrAABuii

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: administrator; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.administrator (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    person_id uuid NOT NULL,
    job_title character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone,
    department_id uuid
);


ALTER TABLE public.administrator OWNER TO postgres;

--
-- Name: building; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.building (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    build_code character varying(20) NOT NULL,
    floor integer NOT NULL,
    campus character varying(30) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    department_id uuid
);


ALTER TABLE public.building OWNER TO postgres;

--
-- Name: course; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    acg character varying(100),
    schedule character varying(300),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    name character varying,
    department_id uuid
);


ALTER TABLE public.course OWNER TO postgres;

--
-- Name: course_lecture; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course_lecture (
    course_id uuid NOT NULL,
    lecture_id uuid NOT NULL
);


ALTER TABLE public.course_lecture OWNER TO postgres;

--
-- Name: course_student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course_student (
    course_id uuid NOT NULL,
    student_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.course_student OWNER TO postgres;

--
-- Name: credential; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credential (
    id uuid NOT NULL,
    email character varying NOT NULL,
    password character varying NOT NULL,
    last_login timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    person_id uuid NOT NULL,
    refresh_token character varying,
    refresh_token_expiration timestamp without time zone,
    role character varying
);


ALTER TABLE public.credential OWNER TO postgres;

--
-- Name: department; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.department (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100) NOT NULL,
    abbreviation character varying(20),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.department OWNER TO postgres;

--
-- Name: equipment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipment (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(100) NOT NULL,
    description text,
    quantity integer NOT NULL,
    status character varying(30) DEFAULT 'Available'::character varying NOT NULL,
    max_loan_duration integer NOT NULL,
    image_url text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT equipment_max_loan_duration_check CHECK ((max_loan_duration > 0)),
    CONSTRAINT equipment_quantity_check CHECK ((quantity >= 0))
);


ALTER TABLE public.equipment OWNER TO postgres;

--
-- Name: lecture; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lecture (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    subject_name character varying(100) NOT NULL,
    professor_id uuid NOT NULL,
    room_id uuid NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.lecture OWNER TO postgres;

--
-- Name: lecture_students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lecture_students (
    lecture_id uuid NOT NULL,
    student_id uuid NOT NULL
);


ALTER TABLE public.lecture_students OWNER TO postgres;

--
-- Name: notification; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    person_id uuid,
    title character varying(150) NOT NULL,
    message text NOT NULL,
    sent_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    read_at timestamp without time zone
);


ALTER TABLE public.notification OWNER TO postgres;

--
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person (
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    id uuid NOT NULL,
    name character varying,
    phone_number character varying,
    cpf character varying(11) NOT NULL,
    birth_date date,
    profile_url text,
    description character varying(400),
    desactivated_at timestamp without time zone,
    person_code character varying(7),
    role character varying
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: professor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.professor (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    person_id uuid NOT NULL,
    department_id uuid,
    academic_degree character varying(20),
    expertise_area character varying(100),
    employment_status character varying(20),
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone
);


ALTER TABLE public.professor OWNER TO postgres;

--
-- Name: reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reservation (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    person_id uuid NOT NULL,
    equipment_id uuid,
    room_id uuid,
    resource_type character varying(30) NOT NULL,
    purpose character varying(50) NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    status character varying(20) DEFAULT 'Pending'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.reservation OWNER TO postgres;

--
-- Name: room; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    room_code character varying(100) NOT NULL,
    capacity integer NOT NULL,
    room_type character varying(50) NOT NULL,
    code character varying(20) NOT NULL,
    status character varying(30) DEFAULT 'FREE'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    image_url text,
    building_id uuid NOT NULL,
    floor integer NOT NULL,
    CONSTRAINT room_capacity_check CHECK ((capacity > 0)),
    CONSTRAINT room_room_type_check CHECK (((room_type)::text = ANY ((ARRAY['CLASS'::character varying, 'LAB'::character varying, 'AUDITORIUM'::character varying])::text[]))),
    CONSTRAINT room_status_check CHECK (((status)::text = ANY ((ARRAY['FREE'::character varying, 'PARTIALLY_OCCUPIED'::character varying, 'OCCUPIED'::character varying])::text[])))
);


ALTER TABLE public.room OWNER TO postgres;

--
-- Name: student; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    person_id uuid NOT NULL,
    registration_number character varying(20) NOT NULL,
    course character varying(100),
    semester character varying(10),
    shift character varying(10),
    scholarship_type character varying(10),
    academic_status character varying(10),
    created_at timestamp without time zone DEFAULT now(),
    updated_at timestamp without time zone,
    CONSTRAINT scholarship_type_check CHECK (((scholarship_type)::text = ANY ((ARRAY['None'::character varying, 'Partial'::character varying, 'Full'::character varying])::text[]))),
    CONSTRAINT shift_check CHECK (((shift)::text = ANY ((ARRAY['Morning'::character varying, 'Afternoon'::character varying, 'Night'::character varying])::text[])))
);


ALTER TABLE public.student OWNER TO postgres;

--
-- Data for Name: administrator; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.administrator (id, person_id, job_title, created_at, updated_at, department_id) FROM stdin;
c179b703-caaf-4e97-bb09-f7df4d93261e	eafc30d0-1552-41b0-a3fb-05ba664cd084	Gerente de TI	2025-10-25 23:42:58.268	2025-10-25 23:42:58.268	a3bb189e-8bf9-3888-9912-ace4e6543002
\.


--
-- Data for Name: building; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.building (id, build_code, floor, campus, created_at, updated_at, department_id) FROM stdin;
01ad3ee3-e675-46be-b4dc-6007b1f01108	34	2	Coração Eucaristico	2025-10-26 09:02:57.945446	2025-10-26 09:02:57.945446	a3bb189e-8bf9-3888-9912-ace4e6543002
\.


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.course (id, acg, schedule, created_at, updated_at, name, department_id) FROM stdin;
\.


--
-- Data for Name: course_lecture; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.course_lecture (course_id, lecture_id) FROM stdin;
\.


--
-- Data for Name: course_student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.course_student (course_id, student_id, created_at, updated_at) FROM stdin;
\.


--
-- Data for Name: credential; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.credential (id, email, password, last_login, created_at, updated_at, person_id, refresh_token, refresh_token_expiration, role) FROM stdin;
cd7578ed-59bc-48b8-95af-0177e6c3e08c	usuario@example.com	$2a$12$QL/efgkKMvr85DxN9dt7uOHVPPiOLIUYGBTxPUDjnJAxHOnK5B9bO	\N	2025-10-25 23:13:15.93	2025-10-25 23:29:20.809	e548e9bb-837f-4df2-ab8b-e340b1d7660d	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJlNTQ4ZTliYi04MzdmLTRkZjItYWI4Yi1lMzQwYjFkNzY2MGQiLCJyb2xlIjoiU1RVREVOVCIsImlhdCI6MTc2MTQ0NTc2MCwiZXhwIjoxNzYyMDUwNTYwfQ.0kFe3k3uzREx7ETA-Fw4AzBlcNtUnknIumG9Le4VG9w	2025-11-01 23:29:20.809372	STUDENT
b1c4cccf-f2a7-4bf6-8fc7-7c5bd449e4a7	adm@example.com	$2a$12$NkQCMa1CwDuQZJv00SqGdOq6vqraiOScnI7E6QM8MNvGmSVEuI/gK	\N	2025-10-25 23:57:49.156	2025-10-27 17:11:55.143	eafc30d0-1552-41b0-a3fb-05ba664cd084	eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJlYWZjMzBkMC0xNTUyLTQxYjAtYTNmYi0wNWJhNjY0Y2QwODQiLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3NjE1OTU5MTUsImV4cCI6MTc2MjIwMDcxNX0.QDfGQpUwgZuOjHUHjBpxXuuGljuJFBLRYoJA2E2lQas	2025-11-03 17:11:55.142125	ADMIN
\.


--
-- Data for Name: department; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.department (id, name, abbreviation, created_at, updated_at) FROM stdin;
a3bb189e-8bf9-3888-9912-ace4e6543002	teste	tst	2025-10-17 00:35:00	2025-10-17 00:35:00
\.


--
-- Data for Name: equipment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipment (id, name, description, quantity, status, max_loan_duration, image_url, created_at, updated_at) FROM stdin;
6233af9b-b82f-4135-b6e2-7f5c058b694f	Fone de ouvido 2	Fone bom de usar	5	Available	7	https://example.com/images/projetor.jpg	2025-10-26 00:03:48.673	2025-10-26 11:00:54.051
\.


--
-- Data for Name: lecture; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lecture (id, subject_name, professor_id, room_id, start_time, end_time, created_at, updated_at) FROM stdin;
\.


--
-- Data for Name: lecture_students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lecture_students (lecture_id, student_id) FROM stdin;
\.


--
-- Data for Name: notification; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notification (id, person_id, title, message, sent_at, created_at, updated_at, read_at) FROM stdin;
418d1da6-838e-40bf-9caf-3073f4c86b26	\N	Novo item cadastrado	Equipamento: Projetor Epson cadastrado no sistema.	2025-10-26 00:03:48.715	2025-10-26 00:03:48.715	2025-10-26 00:03:48.715	\N
e42d606e-9910-48e3-9db4-08754fef2d0c	\N	Nova sala cadastrada	Sala: LAB101 cadastrado no sistema.	2025-10-26 09:07:17.457	2025-10-26 09:07:17.457	2025-10-26 09:07:17.457	\N
92d8a8d8-bcbc-4d18-963c-8121700d8bd6	eafc30d0-1552-41b0-a3fb-05ba664cd084	Reserva de equipamento rejeitada!	O pedido do item Projetor Epson foi rejeitado!	2025-10-26 09:49:48.728	2025-10-26 09:49:48.728	2025-10-26 09:49:48.728	\N
f5350af9-12ba-4b88-a534-e1eda077dbc1	\N	Novo item cadastrado	Equipamento: Fone de ouvido cadastrado no sistema.	2025-10-26 09:59:48.701	2025-10-26 09:59:48.701	2025-10-26 09:59:48.701	\N
fa36b951-a3fe-4403-8ab3-18ad42a7ab3e	eafc30d0-1552-41b0-a3fb-05ba664cd084	Reserva de equipamento aprovada!	O pedido do item Fone de ouvido foi aprovado!	2025-10-26 10:02:10.548	2025-10-26 10:02:10.548	2025-10-26 10:02:10.548	\N
755b0dc5-9fd5-46aa-bea5-cb90ca88bca9	\N	Nova aula cadastrada	Nova aula de: Introdução à Programação	2025-10-26 10:34:34.933	2025-10-26 10:34:34.933	2025-10-26 10:34:34.933	\N
23ac2d66-586e-4c09-a8e9-1d9678387bb8	\N	Nova sala cadastrada	Sala: LAB101 cadastrado no sistema.	2025-10-26 11:01:48.897	2025-10-26 11:01:48.897	2025-10-26 11:01:48.897	\N
ad498280-4277-42b3-91fe-d85b86caf314	\N	Nova sala cadastrada	Sala: LAB102 cadastrado no sistema.	2025-10-27 17:13:55.876	2025-10-27 17:13:55.876	2025-10-27 17:13:55.876	\N
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (created_at, updated_at, id, name, phone_number, cpf, birth_date, profile_url, description, desactivated_at, person_code, role) FROM stdin;
2025-10-25 23:10:26.195	2025-10-25 23:10:26.195	e548e9bb-837f-4df2-ab8b-e340b1d7660d	João Silva	+55 31 91234-5678	12345678900	1990-05-15	https://example.com/profile/joaosilva	Estudante de engenharia apaixonado por tecnologia.	\N	P123456	STUDENT
2025-10-25 23:42:58.247	2025-10-25 23:42:58.247	eafc30d0-1552-41b0-a3fb-05ba664cd084	João Silva	+55 31 91234-5678	11111111111	1990-05-15	https://example.com/profile/joaosilva	Estudante de engenharia apaixonado por tecnologia.	\N	P123456	ADMIN
2025-10-25 23:50:26.87	2025-10-25 23:50:26.87	700d4a27-5f6a-47d0-9b76-b594efdd01d3	Maria Oliveira	+5511988887777	98765432100	1985-09-20	https://example.com/profiles/maria.jpg	Professora de Matemática	\N	P002	PROFESSOR
\.


--
-- Data for Name: professor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.professor (id, person_id, department_id, academic_degree, expertise_area, employment_status, created_at, updated_at) FROM stdin;
1a95ab0e-e4f5-48b4-95c4-290f29dc5717	700d4a27-5f6a-47d0-9b76-b594efdd01d3	a3bb189e-8bf9-3888-9912-ace4e6543002	Doutorado	Matemática Aplicada	Efetivo	2025-10-25 23:50:26.88	2025-10-25 23:50:26.88
\.


--
-- Data for Name: reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reservation (id, person_id, equipment_id, room_id, resource_type, purpose, start_time, end_time, status, created_at, updated_at) FROM stdin;
eed2e481-2917-4b7e-9ed9-919b89a5e4aa	eafc30d0-1552-41b0-a3fb-05ba664cd084	6233af9b-b82f-4135-b6e2-7f5c058b694f	\N	Equipment	Apresentação de projeto de pesquisa	2025-10-29 06:30:00	2025-10-29 08:10:00	Rejected	2025-10-26 09:22:51.789	2025-10-26 09:49:38.779
7bff6715-7326-4014-8319-e274e9b690a2	eafc30d0-1552-41b0-a3fb-05ba664cd084	\N	84d0a668-a8c7-430d-9870-0fde75704a6e	Room	Apresentação de projeto de pesquisa	2025-10-29 06:30:00	2025-10-29 08:10:00	Approved	2025-10-27 17:15:56.192	2025-10-27 17:15:56.192
\.


--
-- Data for Name: room; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room (id, room_code, capacity, room_type, code, status, created_at, updated_at, image_url, building_id, floor) FROM stdin;
84d0a668-a8c7-430d-9870-0fde75704a6e	SALA104	35	LAB	LAB101	FREE	2025-10-26 11:01:48.872	2025-10-26 11:02:26.054	https://example.com/images/lab101_updated.jpg	01ad3ee3-e675-46be-b4dc-6007b1f01108	1
bae42bf3-13d1-4d0f-b7a7-c45a1767059b	SALA102	35	LAB	LAB102	FREE	2025-10-27 17:13:55.853	2025-10-27 17:13:55.853	https://example.com/images/lab101_updated.jpg	01ad3ee3-e675-46be-b4dc-6007b1f01108	1
\.


--
-- Data for Name: student; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student (id, person_id, registration_number, course, semester, shift, scholarship_type, academic_status, created_at, updated_at) FROM stdin;
52054104-8448-464e-b083-e1976ab75210	e548e9bb-837f-4df2-ab8b-e340b1d7660d	2023123456	\N	5	Morning	Partial	Ativo	2025-10-25 23:10:26.203	2025-10-25 23:10:26.203
\.


--
-- Name: administrator administrator_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT administrator_pkey PRIMARY KEY (id);


--
-- Name: building building_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.building
    ADD CONSTRAINT building_pkey PRIMARY KEY (id);


--
-- Name: course_lecture course_lecture_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_lecture
    ADD CONSTRAINT course_lecture_pkey PRIMARY KEY (course_id, lecture_id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: course_student course_student_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_student
    ADD CONSTRAINT course_student_pkey PRIMARY KEY (course_id, student_id);


--
-- Name: credential credential_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credential
    ADD CONSTRAINT credential_pk PRIMARY KEY (id);


--
-- Name: department department_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.department
    ADD CONSTRAINT department_pkey PRIMARY KEY (id);


--
-- Name: equipment equipment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment
    ADD CONSTRAINT equipment_pkey PRIMARY KEY (id);


--
-- Name: lecture lecture_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT lecture_pkey PRIMARY KEY (id);


--
-- Name: lecture_students lecture_students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lecture_students
    ADD CONSTRAINT lecture_students_pkey PRIMARY KEY (lecture_id, student_id);


--
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- Name: person person_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pk PRIMARY KEY (id);


--
-- Name: person person_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_unique UNIQUE (cpf);


--
-- Name: professor professor_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professor
    ADD CONSTRAINT professor_pk PRIMARY KEY (id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: room room_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_code_key UNIQUE (code);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (id);


--
-- Name: student student_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pk PRIMARY KEY (id);


--
-- Name: student student_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_unique UNIQUE (registration_number);


--
-- Name: administrator administrator_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT administrator_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.department(id) ON DELETE SET NULL;


--
-- Name: administrator administrator_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT administrator_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE CASCADE;


--
-- Name: building building_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.building
    ADD CONSTRAINT building_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.department(id) ON DELETE SET NULL;


--
-- Name: course course_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.department(id) ON DELETE SET NULL;


--
-- Name: course_lecture course_lecture_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_lecture
    ADD CONSTRAINT course_lecture_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.course(id) ON DELETE CASCADE;


--
-- Name: course_lecture course_lecture_lecture_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_lecture
    ADD CONSTRAINT course_lecture_lecture_id_fkey FOREIGN KEY (lecture_id) REFERENCES public.lecture(id) ON DELETE CASCADE;


--
-- Name: course_student course_student_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_student
    ADD CONSTRAINT course_student_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.course(id) ON DELETE CASCADE;


--
-- Name: course_student course_student_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_student
    ADD CONSTRAINT course_student_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.student(id) ON DELETE CASCADE;


--
-- Name: credential fk_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.credential
    ADD CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE CASCADE;


--
-- Name: student fk_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student
    ADD CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE CASCADE;


--
-- Name: professor fk_professor_department; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professor
    ADD CONSTRAINT fk_professor_department FOREIGN KEY (department_id) REFERENCES public.department(id) ON DELETE SET NULL;


--
-- Name: professor fk_professor_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professor
    ADD CONSTRAINT fk_professor_person FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE CASCADE;


--
-- Name: lecture lecture_professor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT lecture_professor_id_fkey FOREIGN KEY (professor_id) REFERENCES public.professor(id);


--
-- Name: lecture lecture_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lecture
    ADD CONSTRAINT lecture_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.room(id);


--
-- Name: lecture_students lecture_students_lecture_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lecture_students
    ADD CONSTRAINT lecture_students_lecture_id_fkey FOREIGN KEY (lecture_id) REFERENCES public.lecture(id) ON DELETE CASCADE;


--
-- Name: lecture_students lecture_students_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lecture_students
    ADD CONSTRAINT lecture_students_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.student(id) ON DELETE CASCADE;


--
-- Name: notification notification_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- Name: reservation reservation_equipment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES public.equipment(id) ON DELETE CASCADE;


--
-- Name: reservation reservation_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id);


--
-- Name: reservation reservation_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.room(id) ON DELETE CASCADE;


--
-- Name: room room_building_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_building_fk FOREIGN KEY (building_id) REFERENCES public.building(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict RbCuI3hHQhBeZrFEt56dl2MOLFJeJ4VKIqW7B2yGFGHkbP6u6mT30o6NrAABuii

