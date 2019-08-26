// MyEventBusService.aidl
package com.example.xeventbus;

// Declare any non-default types here with import statements
import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
interface MyEventBusService {

    Response send(in Request request);

}
