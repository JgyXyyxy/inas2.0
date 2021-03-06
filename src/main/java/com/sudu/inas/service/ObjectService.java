package com.sudu.inas.service;

import com.sudu.inas.beans.Entity;
import com.sudu.inas.beans.RealEntity;

import java.util.List;

/**
 * Created by J on  17-10-27.
 */

public interface ObjectService {

    Entity findObjectById(String objectId) throws Exception;

    RealEntity findEntityById(String objectId) throws Exception;

    RealEntity findEntityByIdFromEs(String objectId) throws Exception;

    List<Entity> findObjectsByPrefix(String prefix) throws Exception;

    List<RealEntity> findEntitiesByPrefix(String prefix) throws Exception;

}
