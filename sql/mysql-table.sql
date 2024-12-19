/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.32-log : Database - test
*********************************************************************
*/

CREATE DATABASE /*!32312 IF NOT EXISTS*/`test` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `test`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL COMMENT '主键 ID',
                        `name` varchar(30) DEFAULT NULL,
                        `age` int(11) DEFAULT NULL COMMENT '年龄',
                        `email` varchar(50) DEFAULT NULL,
                        `create_date` datetime DEFAULT NULL,
                        `update_date` datetime DEFAULT NULL,
                        `version` int(11) DEFAULT NULL,
                        `is_deleted` int(11) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

LOCK TABLES `user` WRITE;

insert  into `user`(`id`,`name`,`age`,`email`,`create_date`,`update_date`,`version`,`is_deleted`)
values
(2,'xxx',11,'12423@qq.com',NULL,NULL,NULL,0),
(3,'小反',18,'2341@qq.com',NULL,NULL,NULL,0),
(4,'小红',11,'12423@qq.com',NULL,NULL,NULL,1),
(8,'zyl',20,NULL,NULL,NULL,NULL,NULL),
(10,'zyl10',2010,NULL,NULL,NULL,NULL,NULL),
(11,'zyl11',2011,NULL,NULL,NULL,NULL,NULL),
(12,'zyl12',2012,NULL,NULL,NULL,NULL,NULL);

UNLOCK TABLES;

