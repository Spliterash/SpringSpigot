package ru.spliterash.springspigot.json.annotations.minecraftColor;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@JsonDeserialize(using = MinecraftDeserializeConverter.class)
@JsonSerialize(using = MinecraftSerializeConverter.class)
@Retention(RUNTIME)
@Target(FIELD)
@JacksonAnnotationsInside
public @interface MinecraftColorText {
}