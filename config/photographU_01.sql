drop database if exists db_photograph_u;
create database db_photograph_u;
use db_photograph_u;

create table user (
  id         int                 not null auto_increment,
  nickname   char(15)            not null default '小柚子',
  head_image varchar(100)                 default 'default.jpg',
  sex        char(1)                      default '*',
  birthday   date                         default '2018-06-01',
  phone      char(11)            not null,
  password   char(33)            not null,
  school     char(20)            not null default '西华大学',
  is_deleted tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '用户';
#insert into user (nickname, head_image, sex, birthday, phone, password)values ('技术不宅', 'default.jpg', '男', '1996-12-28', '18483638749', '982352');

create table photographer (
  id             int                 not null auto_increment,
  user_id        int                 not null,
  card_no        char(15)            not null,
  card_image     varchar(100)        not null,
  price          decimal(9, 2)       not null default 0.00,
  server_content varchar(100)        not null default '暂未填写',
  introduce      varchar(100),
  star_value     int                 not null default 0,
  is_deleted     tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '摄影师';

create table note (
  id           int                 not null auto_increment,
  user_id      int                 not null,
  style_id     int                 not null,
  content      varchar(100)        not null,
  release_time datetime            not null,
  is_deleted   tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '帖子';

create table admire (
  id         int                 not null auto_increment,
  user_id    int                 not null,
  note_id    int                 not null,
  is_deleted tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '点赞';

create table comment (
  id         int                 not null auto_increment,
  user_id    int                 not null,
  note_id    int                 not null,
  content    varchar(100)        not null,
  father_id  int                 not null,
  is_deleted tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '评论';

create table orderinfo (
  id              int                 not null auto_increment,
  user_id         int                 not null,
  photographer_id int                 not null,
  order_time      datetime            not null,
  address         varchar(50)         not null,
  other           varchar(50)         not null,
  state           int unsigned          default 0,
  is_deleted      tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '预约信息';

create table review (
  id              int                 not null auto_increment,
  user_id         int                 not null,
  photographer_id int                 not null,
  content         varchar(100)        not null,
  is_deleted      tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '评价';

create table follow (
  id              int                 not null auto_increment,
  user_id         int                 not null,
  photographer_id int                 not null,
  is_deleted      tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '关注';

create table style (
  id         int                 not null auto_increment,
  name       char(8)             not null,
  image      varchar(100)        not null,
  is_deleted tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '风格';
insert into style (name, image) values ('毕业照', 'default.jpg');
insert into style (name, image) values ('古装', 'default.jpg');
insert into style (name, image) values ('个人写真', 'default.jpg');
insert into style (name, image) values ('CosPlay', 'default.jpg');
insert into style (name, image) values ('活动会议', 'default.jpg');
insert into style (name, image) values ('个人写真', 'default.jpg');
insert into style (name, image) values ('商业圈', 'default.jpg');

create table admin (
  id         int                 not null auto_increment,
  account    char(20)            not null,
  password   char(33)            not null,
  is_deleted tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '管理员';
  insert into admin(account,password) values('photograph_u','123456');

create table styleinfo (
  id              int                 not null auto_increment,
  photographer_id int                 not null,
  style_id        int                 not null,
  is_deleted      tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '风格设置信息';

create table image (
  id         int                 not null auto_increment,
  note_id    int                 not null,
  name       varchar(100)        not null,
  is_deleted tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '帖子图片';

create table photo (
  id              int                 not null auto_increment,
  photographer_id int                 not null,
  name            varchar(100)        not null,
  is_deleted      tinyint(1) unsigned not null default 0,
  primary key (id)
)
  comment '摄影师照片';
create table verify (
  id         int                 not null   auto_increment,
  user_id    int                 not null,
  admin_id   int                 not null default 1,
  card_no    char(15)            not null,
  card_image varchar(100),
  submit_time datetime           not null,
  verify_time datetime,
  state      int unsigned            default 0,
  is_deleted tinyint(1) unsigned not null   default 0,
  primary key (id)
)
  comment '验证信息';



