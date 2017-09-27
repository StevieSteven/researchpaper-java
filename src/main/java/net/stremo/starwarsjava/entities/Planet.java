package net.stremo.starwarsjava.entities;

import org.h2.table.Plan;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planets")
public class Planet extends BaseEntity {


    private String name;
    private String diameter;
    private String rotation_period;
    private String gravity;
    private String population;
    private String climate;
    private String terrain;
    private String surface_water;
    private String created;
    private String edited;


    @ManyToMany(mappedBy = "planets")
    private List<Film> films = new ArrayList<>();

//   todo     adding     "orbital_period"

    public Planet(){}


    public Planet(String name, String diameter, String rotation_period, String gravity, String population, String climate, String terrain, String surface_water, String created, String edited) {
        this.name = name;
        this.diameter = diameter;
        this.rotation_period = rotation_period;
        this.gravity = gravity;
        this.population = population;
        this.climate = climate;
        this.terrain = terrain;
        this.surface_water = surface_water;
        this.created = created;
        this.edited = edited;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getRotation_period() {
        return rotation_period;
    }

    public void setRotation_period(String rotation_period) {
        this.rotation_period = rotation_period;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getSurface_water() {
        return surface_water;
    }

    public void setSurface_water(String surface_water) {
        this.surface_water = surface_water;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

//    @Override
//    public String toString() {
//        return "Planet{" +
//                "id='" + getId() + '\'' +
//                "name='" + name + '\'' +
//                ", diameter='" + diameter + '\'' +
//                ", rotation_period='" + rotation_period + '\'' +
//                ", gravity='" + gravity + '\'' +
//                ", population='" + population + '\'' +
//                ", climate='" + climate + '\'' +
//                ", terrain='" + terrain + '\'' +
//                ", surface_water='" + surface_water + '\'' +
//                ", created='" + created + '\'' +
//                ", edited='" + edited + '\'' +
//                '}';
//    }
}
