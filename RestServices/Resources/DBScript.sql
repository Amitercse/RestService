create database test_db;

================================ Message table =================================
create table message(id integer, message varchar(50), author_name varchar(20), posted_date timestamp, primary key(id));
insert into message values(1, "first message posted by author1", "author1", now());
insert into message values(2, "second message posted by author1", "author1", now());
insert into message values(3, "first message posted by author2", "author2", now());
insert into message values(4, "second message posted by author2", "author2", now());

================================ Comments table =================================
create table comment(comment_id integer, comment varchar(50), author_name varchar(20), posted_date timestamp, message_id integer,
foreign key(message_id) references message(id) on delete cascade);

insert into comment values(1, "first comment on message id 1", "author1", now(), 1);
insert into comment values(2, "second comment on message id 1", "author1", now(), 1);
insert into comment values(3, "first comment on message id 2", "author2", now(), 2);
insert into comment values(4, "second comment on message id 2", "author2", now(), 2);
