package com.example.appdietas.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ComidaDao {
    @Insert
    long insert(Comida comida);

    @Update
    void update(Comida comida);

    @Delete
    void delete(Comida comida);

    @Query("SELECT * FROM comidas WHERE id = :id")
    Comida getById(long id);

    @Query("SELECT * FROM comidas")
    List<Comida> getAll();

    @Query("SELECT * FROM comidas WHERE tipo = :tipo ORDER BY RANDOM() LIMIT 1")
    Comida getRandomByTipo(String tipo);
}
