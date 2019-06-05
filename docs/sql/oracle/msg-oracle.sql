-- Create table
create table notify_http_logs
(
  msg_id      varchar2(50) not null,
  retry_nums  number(8,0) not null,
  total_nums  number(8,0),
  delay       number(16,0),
  result      number(1,0),
  url         clob,
  type        varchar2(255),
  data        clob,
  create_time timestamp,
  update_time timestamp
)
;
-- Add comments to the table
comment on table notify_http_logs
  is 'Http异步通知日志';
-- Add comments to the columns
comment on column notify_http_logs.retry_nums
  is '重试次数';
comment on column notify_http_logs.total_nums
  is '通知总次数';
comment on column notify_http_logs.delay
  is '延迟时间';
comment on column notify_http_logs.result
  is '通知结果';
comment on column notify_http_logs.url
  is '通知地址';
comment on column notify_http_logs.type
  is '通知类型';
comment on column notify_http_logs.data
  is '请求数据';
