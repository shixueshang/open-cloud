-- Create table
create sequence scheduler_job_logs_seq;
create table scheduler_job_logs
(
  log_id          number(18) not null,
  job_name        varchar2(255),
  job_group       varchar2(255),
  job_class       varchar2(255),
  job_description varchar2(255),
  trigger_class   varchar2(255),
  cron_expression varchar2(255),
  run_time        number(20),
  start_time      timestamp,
  end_time        timestamp,
  create_time     timestamp,
  job_data        timestamp,
  exception       clob,
  status          number(3) default 0
)
;
-- Add comments to the table 
comment on table scheduler_job_logs
  is '任务调度日志';
-- Add comments to the columns 
comment on column scheduler_job_logs.job_name
  is '任务名称';
comment on column scheduler_job_logs.job_group
  is '任务组名';
comment on column scheduler_job_logs.job_class
  is '任务执行类';
comment on column scheduler_job_logs.job_description
  is '任务描述';
comment on column scheduler_job_logs.trigger_class
  is '任务触发器';
comment on column scheduler_job_logs.cron_expression
  is '任务表达式';
comment on column scheduler_job_logs.run_time
  is '运行时间';
comment on column scheduler_job_logs.start_time
  is '开始时间';
comment on column scheduler_job_logs.end_time
  is '结束时间';
comment on column scheduler_job_logs.create_time
  is '日志创建时间';
comment on column scheduler_job_logs.job_data
  is '任务执行数据';
comment on column scheduler_job_logs.exception
  is '异常';
comment on column scheduler_job_logs.status
  is '状态：0-失败 1-成功';
alter table scheduler_job_logs
  add constraint PK_scheduler_job_logs_ID primary key (log_id);