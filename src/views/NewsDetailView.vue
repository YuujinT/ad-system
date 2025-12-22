<template>
  <div class="page-container">
    <div class="main-content">
      <button @click="goBack" class="back-button">&laquo; 返回</button>
      
      <div class="news-content" v-if="currentNews">
        <h1 class="page-title">{{ currentNews.title }}</h1>
        
        <div class="news-meta">
          <span class="category-tag">{{ getCategoryName(currentNews.category) }}</span>
          <span class="date">{{ currentNews.date || '2023-04-01' }}</span>
          <span class="author">作者: {{ currentNews.author || '匿名' }}</span>
        </div>
        
        <!-- 广告展示部分 -->
        <div class="ad-section">
          <div id="adContainer" class="ad-placeholder">
            <h3>广告位</h3>
            <p>这里将展示广告内容</p>
          </div>
        </div>
        
        <div class="content">
          <p>{{ currentNews.content }}</p>
          
          <p>这是新闻的详细内容。在这里您可以阅读完整的文章。新闻内容包括事件的详细描述、相关背景信息以及专家观点等。我们致力于为读者提供准确、全面的新闻报道。</p>
          
          <p>更多相关内容将继续更新。请持续关注我们的网站以获取最新的新闻资讯。我们专业的记者团队会第一时间为您带来世界各地的重要新闻。</p>
          
          <p>如果您对这则新闻有任何看法或意见，欢迎在下方留言区与我们互动。您的反馈对我们非常重要，有助于我们改善新闻质量和服务水平。</p>
        </div>
      </div>
      
      <div v-else class="not-found">
        <h2>新闻未找到</h2>
        <p>抱歉，您查找的新闻不存在。</p>
      </div>
    </div>
  </div>
</template>

<script src="./NewsDetailView.js"></script>

<style src="./NewsDetailView.css" scoped></style>