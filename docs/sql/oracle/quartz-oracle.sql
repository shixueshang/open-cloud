-- Create table
create table QRTZ_BLOB_TRIGGERS
(
  sched_name    varchar2(120),
  trigger_name  varchar2(200),
  trigger_group varchar2(200),
  blob_data     blob
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_BLOB_TRIGGERS
  add constraint PK__BLOB_TRIGGERS_NAME_GROUP primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);



-- Create table
create table QRTZ_CALENDARS
(
  sched_name    varchar2(120),
  calendar_name varchar2(200),
  calendar      blob
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_CALENDARS
  add constraint PK_QRTZ_CALENDARS primary key (SCHED_NAME, CALENDAR_NAME);


-- Create table
create table QRTZ_JOB_DETAILS
(
  sched_name        varchar2(120) not null,
  job_name          varchar2(200) not null,
  job_group         varchar2(200) not null,
  description       varchar2(250),
  job_class_name    varchar2(250) not null,
  is_durable        varchar2(1) not null,
  is_nonconcurrent  varchar2(1) not null,
  is_update_data    varchar2(1) not null,
  requests_recovery varchar2(1) not null,
  job_data          blob
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_JOB_DETAILS
  add constraint PK_JOB_DETAILS_JOB_NAME_GROUP primary key (SCHED_NAME, JOB_NAME, JOB_GROUP);



-- Create table
create table QRTZ_TRIGGERS
(
  sched_name     varchar2(120) not null,
  trigger_name   varchar2(200) not null,
  trigger_group  varchar2(200) not null,
  job_name       varchar2(200) not null,
  job_group      varchar2(200) not null,
  description    varchar2(250),
  next_fire_time number(13),
  prev_fire_time number(13),
  priority       number(11),
  trigger_state  varchar2(16),
  trigger_type   varchar2(8),
  start_time     number(13),
  end_time       number(13),
  calendar_name  varchar2(200),
  misfire_instr  number(2),
  job_data       blob
)
;
-- Create/Recreate indexes
create index idx_QRTZ_TRIGGERS_SCHED_NAME on QRTZ_TRIGGERS (sched_name, job_name, job_group);
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_TRIGGERS
  add constraint PK_QRTZ_TRIGGERS_NAME_GROUP primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
alter table QRTZ_TRIGGERS
  add constraint QRTZ_TRIGGERS_ibfk_1 foreign key (SCHED_NAME, JOB_NAME, JOB_GROUP)
  references QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP);




  -- Create table
create table QRTZ_CRON_TRIGGERS
(
  sched_name      varchar2(120) not null,
  trigger_name    varchar2(200) not null,
  trigger_group   varchar2(200) not null,
  cron_expression varchar2(200) not null,
  time_zone_id    varchar2(80)
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_CRON_TRIGGERS
  add constraint PK_CRON_TRIGGERS_NAME_GROUP primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
alter table QRTZ_CRON_TRIGGERS
  add constraint QRTZ_CRON_TRIGGERS_ibfk_1 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  references QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);



-- Create table
create table QRTZ_FIRED_TRIGGERS
(
  sched_name        varchar2(120) not null,
  entry_id          varchar2(95) not null,
  trigger_name      varchar2(200) not null,
  trigger_group     varchar2(200) not null,
  instance_name     varchar2(200) not null,
  fired_time        number(13) not null,
  sched_time        number(13) not null,
  priority          number(11) not null,
  state             varchar2(16) not null,
  job_name          varchar2(200),
  job_group         varchar2(200),
  is_nonconcurrent  varchar2(1),
  requests_recovery varchar2(1)
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_FIRED_TRIGGERS
  add constraint PK_FIRED_TRIGGERS_NAME_ENTRY primary key (SCHED_NAME, ENTRY_ID);


-- Create table
create table QRTZ_LOCKS
(
  sched_name varchar2(120),
  lock_name  varchar2(40)
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_LOCKS
  add constraint PK_QRTZ_LOCKS primary key (SCHED_NAME, LOCK_NAME);



-- Create table
create table QRTZ_PAUSED_TRIGGER_GRPS
(
  sched_name    varchar2(120) not null,
  trigger_group varchar2(200) not null
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_PAUSED_TRIGGER_GRPS
  add constraint PK_PAUSED_TRI_GRPS_NAME_GROUP primary key (SCHED_NAME, TRIGGER_GROUP);


-- Create table
create table QRTZ_SCHEDULER_STATE
(
  sched_name        varchar2(120) not null,
  instance_name     varchar2(200) not null,
  last_checkin_time number(13) not null,
  checkin_interval  number(13) not null
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_SCHEDULER_STATE
  add constraint PK_SCHEDULER_STATE primary key (SCHED_NAME, INSTANCE_NAME);



-- Create table
create table QRTZ_SIMPLE_TRIGGERS
(
  sched_name      varchar2(120) not null,
  trigger_name    varchar2(200) not null,
  trigger_group   varchar2(200) not null,
  repeat_count    number(7) not null,
  repeat_interval number(12) not null,
  times_triggered number(10) not null
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_SIMPLE_TRIGGERS
  add constraint PK_SIMPLE_TRIGGERS_NAME_GROUP primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
alter table QRTZ_SIMPLE_TRIGGERS
  add constraint QRTZ_SIMPLE_TRIGGERS_ibfk_1 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  references qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);



-- Create table
create table QRTZ_SIMPROP_TRIGGERS
(
  sched_name    varchar2(120) not null,
  trigger_name  varchar2(200) not null,
  trigger_group varchar2(200) not null,
  str_prop_1    varchar2(512),
  str_prop_2    varchar2(512),
  str_prop_3    varchar2(512),
  int_prop_1    number(11),
  int_prop_2    number(11),
  long_prop_1   number(20),
  long_prop_2   number(20),
  dec_prop_1    number(13,4),
  dec_prop_2    number(13,4),
  bool_prop_1   varchar2(1),
  bool_prop_2   varchar2(1)
)
;
-- Create/Recreate primary, unique and foreign key constraints
alter table QRTZ_SIMPROP_TRIGGERS
  add constraint PK_SIMPROP_TRIGGERS_NAME_GROUP primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
alter table QRTZ_SIMPROP_TRIGGERS
  add constraint QRTZ_SIMPROP_TRIGGERS_ibfk_1 foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  references qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);





