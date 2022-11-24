package com.priscilahuentemilla.cbs_versionoriginal.HomeAdapter;

public class FeaturedHelpedClass {

    int image;
    String title, description, autor, genero, paginas;

    public FeaturedHelpedClass(int image, String title, String description, String autor, String genero, String paginas) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.autor = autor;
        this.genero = genero;
        this.paginas = paginas;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAutor() {
        return autor;
    }

    public String getGenero() {
        return genero;
    }

    public String getPaginas() {
        return paginas;
    }
}
