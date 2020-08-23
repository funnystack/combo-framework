package com.funny.combo.encrypt.service;

public class EncryptFactory {

    private static AbstractEncryptService localEncryptService = new LocalEncryptService();

    private static AbstractEncryptService rpcEncryptService = new RpcEncryptService();



    private static AbstractEncryptService getDefaultEncryptService(){
        return localEncryptService;
    }

    public static AbstractEncryptService getEncryptService(String model){
        if(model == null){
            return getDefaultEncryptService();
        }
        if("local".equals(model)){
            return localEncryptService;
        }else if("rpc".equals(model)){
            return rpcEncryptService;
        }
        return localEncryptService;
    }


}
