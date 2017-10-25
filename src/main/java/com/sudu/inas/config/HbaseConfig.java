package com.sudu.inas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


/**
 * Created by J on  17-10-23.
 */

@Configuration
@ImportResource("classpath:hbase-spring.xml")
public class HbaseConfig {
}
