package com.sudu.inas.service;

import com.sudu.inas.beans.Entity;

import java.util.List;

public interface ObjectService {

    Entity findObjectById(String objectId);

    List<Entity> findObjectsByPrefix(String prefix);
}
