package com.example.demo;

import java.lang.reflect.Field;

import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;
        try{
            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target, toInject);
            if(wasPrivate){
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e){
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

	public static CreateUserRequest getAUserRequest() {
		CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("username");
        userRequest.setPassword("password");
        userRequest.setConfirmPassword("password");
        return userRequest;
	}

	public static ModifyCartRequest getAddItemOneRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
    }
    public static ModifyCartRequest getAddItemTwoRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(2);
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
	}
}
