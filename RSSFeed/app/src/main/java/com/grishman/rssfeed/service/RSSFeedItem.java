package com.grishman.rssfeed.service;

 class RSSFeedItem {
     private long articleId;
     private String title;
     private String description;
     private String link;
     private String imgLink;
     private String pubDate;
     private String category;

     public long getArticleId() {
         return articleId;
     }

     public void setArticleId(long articleId) {
         this.articleId = articleId;
     }

     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     public String getLink() {
         return link;
     }

     public void setLink(String link) {
         this.link = link;
     }

     public String getImgLink() {
         return imgLink;
     }

     public void setImgLink(String imgLink) {
         this.imgLink = imgLink;
     }

     public String getPubDate() {
         return pubDate;
     }

     public void setPubDate(String pubDate) {
         this.pubDate = pubDate;
     }

     public String getCategory() {
         return category;
     }

     public void setCategory(String category) {
         this.category = category;
     }
 }
