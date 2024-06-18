desc user;

-- join
insert into user values(null, '관리자', 'admin@mysite.com', password('1234'), 'female', current_date(), 'ADMIN');

-- login
select no, name from user where email = 'gippeum0102@gmail.com' and password=password('1234');

-- update
update dept set name='시스템개발' where no=2;
update user set name='abcd', password=password('abcd'), gender='female' where no = 2;

-- role 추가
alter table user add column role enum('ADMIN', 'USER') not null default 'USER';
update user set role='ADMIN' where no = 1;

-- test
select * from user;


select * from user limit 3, 5;

-- 글 등록
insert into board select null, '제목', '내용', 0, now(), max(g_no) + 1, 1, 1, 3 from board;
insert into board values(null, '제목', '내용', 0, now(), 6, 2, 1, 3);

-- find
select b.no, title, contents, hit, date_format(reg_date, '%Y-%m-%d') as date, g_no, o_no, depth, a.no, a.name  from user a join board b on a.no = b.user_no order by g_no DESC, o_no;
select count(*) from board;

select * from board order by no limit 0, 3;

-- update
update board set title='시스템개발', contents='여러가지를 배워봅시다.', reg_date=now() where no=5 and user_no=1;
update board set o_no=o_no+1 where g_no=1 and o_no>0;

-- delete
delete from board where no = 11 and user_no = 3;

-- test
select * from board;


-- keyword
select b.no as no, title, hit, date_format(reg_date, '%Y-%m-%d %H:%i:%s') as regDate, g_no as groupNo, o_no as orderNo, depth, a.name as userName  
				from user a join board b on a.no = b.user_no where contents like '%123%' limit 5, 5;