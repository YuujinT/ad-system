<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-brand">
        <RouterLink to="/">新闻网站</RouterLink>
      </div>
      <ul class="nav-links">
        <li><RouterLink to="/category/technology">科技</RouterLink></li>
        <li><RouterLink to="/category/gaming">游戏</RouterLink></li>
        <li><RouterLink to="/category/travel">旅游</RouterLink></li>
        <li><RouterLink to="/category/sports">体育</RouterLink></li>
        <li><RouterLink to="/category/food">美食</RouterLink></li>
      </ul>
    </nav>
    
    <!-- 页面内容容器 -->
    <div class="page-wrapper">
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
#app {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  padding: 1rem 2rem;
  flex-shrink: 0;
}

.nav-brand a {
  color: white;
  font-size: 1.5rem;
  text-decoration: none;
}

.nav-links {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 1rem;
}

.nav-links li a {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.nav-links li a:hover,
.nav-links li a.router-link-active {
  background-color: #555;
}

.page-wrapper {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
  padding: 0 20px;
}

@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }
  
  .nav-links {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .page-wrapper {
    padding: 0 15px;
  }
}
</style>