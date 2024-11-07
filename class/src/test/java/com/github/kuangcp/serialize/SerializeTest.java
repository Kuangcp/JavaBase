package com.github.kuangcp.serialize;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

        log.info("content={}", byteOutput.toString());

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());

        ObjectInputStream input = new ObjectInputStream(byteInput);
        Person result = (Person) input.readObject();
        assertThat(result.getName(), equalTo("name"));
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
