 import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import NewsListView from '../views/NewsListView.vue'
import NewsDetailView from '../views/NewsDetailView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/category/:category',
    name: 'category',
    component: NewsListView,
    props: true
  },
  {
    path: '/news/:id',
    name: 'news-detail',
    component: NewsDetailView,
    props: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router