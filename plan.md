## 表结构
1. 访客记录表 bg_visitor 数据表存储用户名，查询访客时，join系统用户表查询到用户昵称
1. 系统用户表 bg_sysuser
1. 角色类型表 bg_role
1. 文章存储表 bg_article
1. 评论存储表 bg_comment
1. 申请提交表 bg_apply 系统用户申请提高权限、系统管理员对申请进行审批
1. 访问记录表 bg_visitrecord 访客操作记录表（点赞、踩；绑定系统用户等）

## 控制器
1. SysUser 登录、注册、修改密码、修改其他信息、退出登录、重置密码、申请提权
1. Article 查询、新增修改、删除
1. Comment 新增、查询（单条、单篇文章）、点赞、踩
1. Visitor 查询、新增、同步为已登录用户信息、修改名称


## 功能
1. mybatis 数据层
1. spring security 权限控制
1. 全局异常捕获
1. 统一返回响应
1. api请求和响应加密（RSA）
1. Swagger嵌入
1. 切面日志记录
1. 单点登录（可配置）：记录浏览器指纹、上一次登录时间、上一次登录IP；异地登录将先前的token置为无效
1. redis
1. xss、sql注入过滤

## 模块（对应package） 
1. 请求内容加解密  crypto

## 白日梦
- Java blog
- Vue 所有的前端页面
- Go websocket（聊天室）、树洞
- net core 本地内网穿透实现
