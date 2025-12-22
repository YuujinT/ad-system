<template>
  <div class="page-container">
    <div class="main-content">
      <h1 class="page-title">{{ categoryName }} 新闻</h1>
      
      <!-- 广告展示部分 -->
      <div class="ad-section">
        <div id="adContainer" class="ad-placeholder">
          <h3>广告位</h3>
          <p>这里将展示广告内容</p>
        </div>
      </div>
      
      <div class="news-container">
        <div 
          v-for="news in filteredNews" 
          :key="news.id" 
          class="news-item"
          @click="goToNewsDetail(news.id)"
        >
          <h2>{{ news.title }}</h2>
          <p>{{ news.summary }}</p>
          <div class="news-meta">
            <span class="date">{{ news.date || '2023-04-01' }}</span>
            <span class="author">作者: {{ news.author || '匿名' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script src="./NewsListView.js"></script>

<style src="./NewsListView.css" scoped></style>