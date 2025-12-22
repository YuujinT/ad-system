export default {
  name: 'HomeView',
  data() {
    return {
      categories: [
        { id: 'technology', name: '科技', description: '最新科技资讯与发展趋势' },
        { id: 'gaming', name: '游戏', description: '热门游戏资讯与评测' },
        { id: 'travel', name: '旅游', description: '旅游攻略与景点推荐' },
        { id: 'sports', name: '体育', description: '体育赛事与运动员动态' },
        { id: 'food', name: '美食', description: '美食制作与餐厅推荐' }
      ],
      newsItems: [
        {
          id: 1,
          title: '人工智能在医疗领域的应用取得重大突破',
          summary: '研究人员开发出新型AI系统，能够更准确地诊断疾病...',
          category: 'technology'
        },
        {
          id: 2,
          title: '新款游戏主机发布，性能提升显著',
          summary: '知名游戏公司发布了新一代游戏主机，画面效果惊艳...',
          category: 'gaming'
        },
        {
          id: 3,
          title: '海岛度假胜地推荐',
          summary: '夏季最佳海岛旅游目的地，享受阳光沙滩...',
          category: 'travel'
        },
        {
          id: 4,
          title: '世界杯预选赛激战正酣',
          summary: '各路豪强争夺世界杯入场券，比赛精彩纷呈...',
          category: 'sports'
        },
        {
          id: 5,
          title: '米其林星级餐厅新榜单出炉',
          summary: '今年新增多家米其林星级餐厅，美食爱好者不容错过...',
          category: 'food'
        }
      ]
    }
  },
  computed: {
    latestNews() {
      return this.newsItems.slice(0, 3)
    }
  },
  methods: {
    goToCategory(category) {
      this.$router.push(`/category/${category}`)
    },
    goToNewsDetail(id) {
      this.$router.push(`/news/${id}`)
    },
    getCategoryName(categoryId) {
      const category = this.categories.find(cat => cat.id === categoryId)
      return category ? category.name : categoryId
    }
  },

  mounted() {
    // 在组件挂载后加载广告
    import('./ad.js').then(adModule => {
      adModule.initAdLoader();
    });
  }
}