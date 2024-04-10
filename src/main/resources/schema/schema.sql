create database if not exists resuDb;
use resuDb;

drop table if exists banMember;
drop table if exists experience;
drop table if exists career;
drop table if exists portfolio;
drop table if exists techStack;
drop table if exists education;
drop table if exists training;
drop table if exists contact;
drop table if exists image;
drop table if exists resume;
drop table if exists member;

create table if not exists member(
                                     memberNo int auto_increment not null primary key ,
                                     email varchar(30) not null,
                                     password varchar(65) null,
                                     name  varchar(20) not null,
                                     phone varchar(20) not null,
                                     birth date not null,
                                     joinDate date not null,
                                     type tinyint(1) not null
);

create table if not exists banMember(
    email varchar(30) not null primary key
);

create table if not exists resume(
                                     resumeNo int auto_increment not null primary key,
                                     memberNo int not null,
                                     type tinyint(1) not null,
                                     name varchar(20) not null,
                                     gender tinyint(1) not null,
                                     birth date not null,
                                     phone varchar(20) not null,
                                     address varchar(100) not null,
                                     intro text not null,
                                     resumeTitle varchar(255) default concat('이력서 ',now()) not null,
                                     basic tinyint(1) not null default false,
                                     foreign key (memberNo) references member(memberNo) on delete cascade

);

## 추가
create table if not exists image (
                                    file_id bigint  not null primary key auto_increment,
                                    identifier int not null,
                                    file_name varchar(255) not null,
                                    uuid  varchar(255)  not null,
                                    upload_date datetime default (now()) not null,
                                    foreign key (identifier) references member(memberNo) on delete cascade
);


create table if not exists experience (
                                          exNo int auto_increment not null primary key,
                                          resumeNo int not null,
                                          exStartDate date not null,
                                          exEndDate date not null,
                                          exName varchar(20) not null,
                                          exDetail text not null,
                                          foreign key (resumeNo) references resume(resumeNo) on delete cascade
);

create table if not exists career (
                                      carNo int auto_increment not null primary key,
                                      resumeNo int not null,
                                      company varchar(20) not null,
                                      dept varchar(20) not null,
                                      position varchar(20) not null,
                                      carStartDate date not null,
                                      carEndDate date not null,
                                      carDetail text not null,
                                      foreign key (resumeNo) references resume(resumeNo) on delete cascade
);

create table if not exists portfolio (
                                         pofolNo int auto_increment not null primary key,
                                         resumeNo int not null,
                                         title varchar(20) not null,
                                         link text not null,
                                         pofolDetail text not null,
                                         foreign key (resumeNo) references resume(resumeNo) on delete cascade
);

create table if not exists techStack (
                                         techNo int auto_increment not null primary key,
                                         resumeNo int not null,
                                         type tinyint(1) not null,
                                         techName varchar(20) not null,
                                         techLevel tinyint(1) not null,
                                         foreign key (resumeNo) references resume(resumeNo) on delete cascade
);

create table if not exists education (
                                         eduNo int auto_increment not null primary key,
                                         resumeNo int not null,
                                         type tinyint(1) not null,
                                         eduName varchar(20) not null,
                                         dept varchar(20) not null,
                                         eduStartDate date not null,
                                         eduEndDate date not null,
                                         eduStatus tinyint(1) not null,
                                         grade double null,
                                         fullGrade double null,
                                         foreign key (resumeNo) references resume(resumeNo) on delete cascade
);

create table if not exists training (
                                        trainNo int auto_increment not null primary key,
                                        resumeNo int not null,
                                        trainName varchar(20) not null,
                                        orgName varchar(20) not null,
                                        trainStartDate date not null,
                                        trainEndDate date not null,
                                        trainDuration int not null,
                                        trainDetail text not null,
                                        foreign key (resumeNo) references resume(resumeNo) on delete cascade
);


create table if not exists contact (
                                       conNo int auto_increment not null primary key,
                                       resumeNo int not null,
                                       type tinyint(1) not null,
                                       contact text not null,
                                       foreign key (resumeNo) references resume(resumeNo) on delete cascade
);