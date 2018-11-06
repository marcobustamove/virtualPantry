package edu.csuci.compsci.virtualpantry;

import java.util.UUID;

public class Pantry
{
    private UUID id;
    private String title;

    public Pantry(String pantryTitle)
    {
        this.id = UUID.randomUUID();
        this.title = pantryTitle;
    }

    public UUID getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.title;
    }

    public void setName(String newTitle)
    {
        this.title = newTitle;
    }




}
