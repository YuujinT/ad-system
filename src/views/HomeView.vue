<template>
  <div class="page-container">
    <div class="main-content">
      <h1 class="page-title">欢迎来到新闻网站</h1>
      
      <!-- 广告展示部分 -->
      <div class="ad-section">
        <!-- 广告容器，供 ad.js 挂载图片广告 -->
        <div id="adContainer" class="ad-placeholder">
          <h3>广告位</h3>
          <p>这里将展示广告内容</p>
        </div>
      </div>
      
      <div class="categories">
        <h2>新闻分类</h2>
        <div class="category-list">
          <div 
            v-for="category in categories" 
            :key="category.id" 
            class="category-item"
            @click="goToCategory(category.id)"
          >
            <h3>{{ category.name }}</h3>
            <p>{{ category.description }}</p>
          </div>
        </div>
      </div>
      
      <div class="latest-news">
        <h2>最新新闻</h2>
        <div class="news-list">
          <div 
            v-for="news in latestNews" 
            :key="news.id" 
            class="news-item"
            @click="goToNewsDetail(news.id)"
          >
            <h3>{{ news.title }}</h3>
            <p>{{ news.summary }}</p>
            <span class="category-tag">{{ getCategoryName(news.category) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 仅导入组件逻辑并导出，Fingerprint 相关在 main.js 全局初始化
import component from './HomeView.js';
export default component;
</script>

<style src="./HomeView.css" scoped></style>