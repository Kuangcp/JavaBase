package com.github.kuangcp.serialize;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by https://github.com/kuangcp on 17-10-24  下午2:27 使用jdk自带的Serializable接口序列化对象 然后反序列化对象
 *
 * @author kuangcp
 */
@Slf4j
public class SerializeTest {

    // 字节数组流
    @Test
    public void testSerializeWithByte() throws IOException, ClassNotFoundException {
        Person person = new Person("name");

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOutput);
        output.writeObject(person);

        log.info("content={}", byteOutput);

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());

        ObjectInputStream input = new ObjectInputStream(byteInput);
        Person result = (Person) input.readObject();
        assertThat(result.getName(), equalTo("name"));
    }

    /**
     * [为什么serialVersionUID不能随便改](https://hollischuang.github.io/toBeTopJavaer/#/basics/java-basic/serialVersionUID-modify?id=%e5%a6%82%e6%9e%9cserialversionuid%e5%8f%98%e4%ba%86%e4%bc%9a%e6%80%8e%e6%a0%b7)
     */
    @Test
    public void testChangeNo() {
        try {
            // Person serialVersionUID 设置为1
            writeFile();

            // Person serialVersionUID 设置为2 再执行
//            readFile();
            // 报错： java.io.InvalidClassException: com.github.kuangcp.serialize.Person; local class incompatible: stream classdesc serialVersionUID = 1, local class serialVersionUID = 2
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Assert.fail();
        }
    }

    /**
     * 序列化对象上某个属性没有实现序列化接口，但该属性没有值
     */
    @Test
    public void testSerializeWithNonSerializableField() throws IOException, ClassNotFoundException {
        Address address = new Address();
        address.setCountry("country");

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOutput);
        output.writeObject(address);

        log.info("content={}", byteOutput.toString());

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());

        ObjectInputStream input = new ObjectInputStream(byteInput);
        Address result = (Address) input.readObject();
        assertThat(result.getCountry(), equalTo("country"));
    }

    /**
     * 序列化对象上某个属性没有实现序列化接口，且该属性有值
     */
    @Test(expected = NotSerializableException.class)
    public void testSerializeWithNonSerializableFieldAndValue() throws IOException {
        Address address = new Address();
        address.setCountry("country");
        Street street = new Street();
        street.setStreet("street");
        address.setStreet(street);

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteOutput);
        output.writeObject(address);
    }

    @Test
    public void testSerializeWithFile() {
        try {
            writeFile();
            readFile();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            Assert.fail();
        }
    }

    private void writeFile() throws IOException {
        Person person = new Person("myth");

        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/person.log");
        ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
        out.writeObject(person);
        out.close();
        fileOutputStream.close();
        log.debug("序列化完成, person={}", person);
    }

    private void readFile() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("/tmp/person.log");
        ObjectInputStream in = new ObjectInputStream(fileInputStream);
        Person object = (Person) in.readObject();
        in.close();
        fileInputStream.close();
        System.out.println(object.toString());
        assertThat(object.getName(), equalTo("myth"));
    }
}
