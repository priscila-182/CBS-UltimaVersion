package com.priscilahuentemilla.cbs_versionoriginal.HomeAdapter;

public class AllBooksHelpedClass {

    int image;
    String title, author, genre, desc, paginas;

    public AllBooksHelpedClass(int image, String title, String author, String genre, String desc, String paginas) {
        this.image = image;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.desc = desc;
        this.paginas = paginas;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getDesc() {
        return desc;
    }

    public String getPaginas() {
        return paginas;
    }
}
