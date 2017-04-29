package mechanic.com.mechanic.BusinessObject;

import java.io.Serializable;

/**
 * Created by Sailesh GB on 4/18/2017.
 */

public class MechanicBO implements Serializable{

    private String idMechanic;
    private String carbon;
    private String silicon;
    private String manganes;
    private String temperature;
    private String poringTemperature;
    private String boxWeight;
    private String materialType;
    private String materialName;


    public String getIdMechanic() {
        return idMechanic;
    }

    public void setIdMechanic(String idMechanic) {
        this.idMechanic = idMechanic;
    }

    public String getCarbon() {
        return carbon;
    }

    public void setCarbon(String carbon) {
        this.carbon = carbon;
    }

    public String getSilicon() {
        return silicon;
    }

    public void setSilicon(String silicon) {
        this.silicon = silicon;
    }

    public String getManganes() {
        return manganes;
    }

    public void setManganes(String manganes) {
        this.manganes = manganes;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getPoringTemperature() {
        return poringTemperature;
    }

    public void setPoringTemperature(String poringTemperature) {
        this.poringTemperature = poringTemperature;
    }

    public String getBoxWeight() {
        return boxWeight;
    }

    public void setBoxWeight(String boxWeight) {
        this.boxWeight = boxWeight;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
