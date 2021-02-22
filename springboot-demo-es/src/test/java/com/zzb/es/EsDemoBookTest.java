package com.zzb.es;

import com.zzb.es.entity.Book;
import com.zzb.es.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 类名称：EsDemoBookTest
 * 类描述：demo1 单元测试
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:43 上午
 * 修改备注：TODO
 */
//@SpringBootTest
class EsDemoBookTest {

    @Autowired
    private BookRepository bookRepository;

    /**
     * 方法：createData
     * 描述：创建测试数据
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:43 上午
     */
    @Test
    void createData() {

        Book book1 = new Book(1, "富婆寻找指南", new BigDecimal("23.33"), Arrays.asList("指南", "富婆"));
        Book book2 = new Book(2, "程序员的头发护理指南", new BigDecimal("28.00"), Arrays.asList("指南", "程序员", "头发"));
        Book book3 = new Book(3, "Java从入门到跑路", new BigDecimal("29.90"), Arrays.asList("Java", "编程", "入门教程"));
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    /**
     * 方法：findByPriceBetween
     * 描述：查询测试数据between
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:43 上午
     */
    @Test
    void findByPriceBetween() {
        List<Book> books = bookRepository.findByPriceBetween(new BigDecimal("15.00"), new BigDecimal("30.00"));
        System.out.println(books);
    }

    /**
     * 方法：findByPriceBetween
     * 描述：查询测试数据 单属性
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:43 上午
     */
    @Test
    void findByTitle() {
        List<Book> books = bookRepository.findByTitle("Java");
        System.out.println(books);
    }

    /**
     * 方法：findByPriceBetween
     * 描述：查询测试数据 in
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:43 上午
     */
    @Test
    void findByTagIn() {
        List<Book> books = bookRepository.findByTagIn(Arrays.asList("Java", "数据库"));
        System.out.println(books);
    }
}