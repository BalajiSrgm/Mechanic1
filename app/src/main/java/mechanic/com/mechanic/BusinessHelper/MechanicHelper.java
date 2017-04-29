package mechanic.com.mechanic.BusinessHelper;

import com.google.firebase.database.DataSnapshot;

import mechanic.com.mechanic.BusinessObject.LoginBO;
import mechanic.com.mechanic.BusinessObject.MechanicBO;
import mechanic.com.mechanic.BusinessObject.StatusBO;
import mechanic.com.mechanic.BusinessObject.UserRoleBO;

/**
 * Created by Sailesh GB on 4/19/2017.
 */

public class MechanicHelper {


    public static MechanicBO databaseToBO(DataSnapshot dataSnapshot1){
        MechanicBO mechanicBO = new MechanicBO();
        String idMechanic = dataSnapshot1.child("idMechanic").getValue(String.class);
        if(StringUtil.isNotNullOrEmpty(idMechanic)) {
            mechanicBO.setIdMechanic(dataSnapshot1.child("idMechanic").getValue(String.class));
            mechanicBO.setCarbon(dataSnapshot1.child("carbon").getValue(String.class));
            mechanicBO.setSilicon(dataSnapshot1.child("silicon").getValue(String.class));
            mechanicBO.setManganes(dataSnapshot1.child("manganes").getValue(String.class));
            mechanicBO.setTemperature(dataSnapshot1.child("temperature").getValue(String.class));
            mechanicBO.setPoringTemperature(dataSnapshot1.child("poringTemperature").getValue(String.class));
            mechanicBO.setBoxWeight(dataSnapshot1.child("boxWeight").getValue(String.class));
            mechanicBO.setMaterialType(dataSnapshot1.child("materialType").getValue(String.class));
            mechanicBO.setMaterialName(dataSnapshot1.child("materialName").getValue(String.class));

        }else{
            mechanicBO = null;
        }

        return mechanicBO;
    }

}
