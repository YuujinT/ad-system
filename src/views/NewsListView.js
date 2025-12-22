export default {
  name: 'NewsListView',
  props: {
    category: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      categories: {
        technology: '科技',
        gaming: '游戏',
        travel: '旅游',
        sports: '体育',
        food: '美食'
      },
      newsItems: [
        {
          id: 1,
          title: '人工智能在医疗领域的应用取得重大突破',
          summary: '研究人员开发出新型AI系统，能够更准确地诊断疾病，有望大幅提高早期癌症检测率。',
          category: 'technology',
          date: '2023-04-15',
          author: '科技日报记者'
        },
        {
          id: 2,
          title: '量子计算技术商业化进程加速',
          summary: '多家科技巨头投入巨资研发量子计算机，预计未来五年内将实现商用化。',
          category: 'technology',
          date: '2023-04-10',
          author: '科技前沿编辑部'
        },
        {
          id: 3,
          title: '新款游戏主机发布，性能提升显著',
          summary: '知名游戏公司发布了新一代游戏主机，画面效果达到电影级水准，支持8K分辨率。',
          category: 'gaming',
          date: '2023-04-12',
          author: '游戏世界'
        },
        {
          id: 4,
          title: '热门游戏推出全新资料片',
          summary: '备受期待的游戏大作发布了最新扩展内容，新增地图和角色获得玩家好评。',
          category: 'gaming',
          date: '2023-04-05',
          author: '游戏评测师'
        },
        {
          id: 5,
          title: '海岛度假胜地推荐',
          summary: '夏季最佳海岛旅游目的地，享受阳光沙滩和清澈海水，提供全方位度假体验。',
          category: 'travel',
          date: '2023-04-14',
          author: '旅游达人'
        },
        {
          id: 6,
          title: '自驾游路线指南：最美风景大道',
          summary: '推荐一条横跨多个国家的风景大道，沿途可欣赏到壮丽的自然景观和人文历史。',
          category: 'travel',
          date: '2023-04-08',
          author: '旅行摄影师'
        },
        {
          id: 7,
          title: '世界杯预选赛激战正酣',
          summary: '各路豪强争夺世界杯入场券，多场关键比赛结果出人意料，球迷热情高涨。',
          category: 'sports',
          date: '2023-04-13',
          author: '体育新闻'
        },
        {
          id: 8,
          title: '网球明星打破历史记录',
          summary: '在最近的比赛中，著名网球选手创造了新的连胜纪录，成为网坛佳话。',
          category: 'sports',
          date: '2023-04-09',
          author: '体育评论员'
        },
        {
          id: 9,
          title: '米其林星级餐厅新榜单出炉',
          summary: '今年新增多家米其林星级餐厅，美食爱好者不容错过这些独特的用餐体验。',
          category: 'food',
          date: '2023-04-11',
          author: '美食家'
        },
        {
          id: 10,
          title: '家庭烘焙技巧分享',
          summary: '专业烘焙师教你如何在家中制作美味蛋糕和面包，步骤简单易学。',
          category: 'food',
          date: '2023-04-06',
          author: '美食生活'
        }
      ]
    }
  },
  computed: {
    categoryName() {
      return this.categories[this.category] || this.category
    },
    filteredNews() {
      return this.newsItems.filter(news => news.category === this.category)
    }
  },
  methods: {
    goToNewsDetail(id) {
      this.$router.push(`/news/${id}`)
    }
  },
  mounted() {
    // 分类页挂载后加载广告
    import('./ad.js').then(adModule => {
      adModule.initAdLoader();
    });
  }
}