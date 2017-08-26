package com.forest.bos.service.system;

import java.util.List;

import com.forest.bos.domain.system.Menu;
import com.forest.bos.domain.system.User;

public interface IMenuService {

	public List<Menu> findAll();

	public void add(Menu model);

	public List<Menu> findByUser(User user);

}
