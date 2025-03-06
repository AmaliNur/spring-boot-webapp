package com.example.cargo;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CargoService {

    @Autowired
    private com.example.cargo.CargoRepository repo;

    public List<Cargo> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    public Cargo get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Cargo save(Cargo cargo) {
        return repo.save(cargo);
    }

    public List<Cargo> sortAll() {
        return repo.sortAll();
    }

    public int countByDay(String keyword) {return repo.countByDay(keyword);}



}

