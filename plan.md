## 表结构
1. 访客记录表 bg_visitor 
1. 系统用户表 bg_sysuser
1. 角色类型表 bg_role
1. 文章存储表 bg_article
1. 评论存储表 bg_comment
1. 申请提交表 bg_apply

## 控制器
1. SysUser 登录、注册、修改密码、修改其他信息、退出登录、重置密码、申请提权
2. Article 查询、新增修改、删除


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

## 模块（对应package） 
1. 请求内容加解密  crypto

## 白日梦
- Java blog
- Vue 所有的前端页面
- Go websocket（聊天室）、树洞
- net core 本地内网穿透实现
