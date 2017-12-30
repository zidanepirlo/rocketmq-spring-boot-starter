package com.yuan.springcloud.enums;

public enum EListenerType {

    CONCURRENTLY("1","CONCURRENTLY"),ORDERLY("2","ORDERLY");

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private EListenerType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

   static public EListenerType getByCode(final String code){

        for (EListenerType eListenerType:EListenerType.values()){

            if (eListenerType.getCode().equals(code)){
                return eListenerType;
            }
        }
        return null;
    }

   static public EListenerType getByDesc(final String desc){

        for (EListenerType eListenerType:EListenerType.values()){

            if (eListenerType.getDesc().equalsIgnoreCase(desc)){
                return eListenerType;
            }
        }
        return null;
    }
}
