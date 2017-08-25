package com.forest.bos.service.system.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forest.bos.dao.system.MenuRepository;
import com.forest.bos.domain.system.Menu;
import com.forest.bos.service.system.IMenuService;

@Service
@Transactional
public class MenuServiceImp implements IMenuService{
	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<Menu> findAll() {
		return menuRepository.findAll();
	}

	@Override
	public void add(Menu model) {
		// TODO Auto-generated method stub
		if (model.getParentMenu()!=null&&model.getParentMenu().getId()==0) {
			System.out.println(model.getId());
			model.setParentMenu(null);
		}
		menuRepository.save(model);
	}
}
