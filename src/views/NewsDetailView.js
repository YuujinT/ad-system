export default {
  name: 'NewsDetailView',
  props: {
    id: {
      type: [String, Number],
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
          category: 'technology',
          date: '2023-04-15',
          author: '科技日报记者',
          content: '这项新技术利用深度学习算法分析医学影像，在多种癌症的早期检测中表现出色。临床试验结果显示，该系统的准确率达到了98.5%，远超传统诊断方法。'
        },
        {
          id: 2,
          title: '量子计算技术商业化进程加速',
          category: 'technology',
          date: '2023-04-10',
          author: '科技前沿编辑部',
          content: '随着量子纠错技术的重大突破，多家科技公司宣布将在未来几年内推出商用量子计算机。这一进展有望彻底改变密码学、药物研发和金融建模等领域。'
        },
        {
          id: 3,
          title: '新款游戏主机发布，性能提升显著',
          category: 'gaming',
          date: '2023-04-12',
          author: '游戏世界',
          content: '这款主机采用了先进的处理器和图形处理单元，支持光线追踪技术和8K分辨率输出。同时，其快速加载时间和创新的手柄设计也为玩家带来了前所未有的游戏体验。'
        },
        {
          id: 4,
          title: '热门游戏推出全新资料片',
          category: 'gaming',
          date: '2023-04-05',
          author: '游戏评测师',
          content: '新资料片增加了超过50小时的游戏内容，包括新的剧情线、角色和装备。玩家可以在全新的开放世界中探险，体验更加丰富的游戏玩法和挑战。'
        },
        {
          id: 5,
          title: '海岛度假胜地推荐',
          category: 'travel',
          date: '2023-04-14',
          author: '旅游达人',
          content: '这些海岛拥有清澈见底的海水、细腻的白沙滩和丰富的海洋生物。游客可以参与潜水、浮潜、帆船等多种水上活动，也可以在海边享受日光浴和海鲜大餐。'
        },
        {
          id: 6,
          title: '自驾游路线指南：最美风景大道',
          category: 'travel',
          date: '2023-04-08',
          author: '旅行摄影师',
          content: '这条路线全长超过1000公里，穿越森林、草原、湖泊和山脉。沿途设有多个观景台和休息站，让游客可以随时停下来欣赏美景和拍摄照片。'
        },
        {
          id: 7,
          title: '世界杯预选赛激战正酣',
          category: 'sports',
          date: '2023-04-13',
          author: '体育新闻',
          content: '本届预选赛出现了许多冷门结果，几支传统强队意外失利，而一些新兴力量则表现出色。这让最终的出线形势变得扑朔迷离，增加了比赛的观赏性。'
        },
        {
          id: 8,
          title: '网球明星打破历史记录',
          category: 'sports',
          date: '2023-04-09',
          author: '体育评论员',
          content: '这位选手不仅在比赛中展现了出色的技术和心理素质，还以优雅的球风和良好的体育精神赢得了观众的喜爱。他的成功为年轻球员树立了榜样。'
        },
        {
          id: 9,
          title: '米其林星级餐厅新榜单出炉',
          category: 'food',
          date: '2023-04-11',
          author: '美食家',
          content: '今年的新榜单反映了餐饮业的发展趋势，融合菜系和可持续发展理念受到更多关注。许多餐厅开始使用本地食材和创新烹饪技法，为食客带来独特体验。'
        },
        {
          id: 10,
          title: '家庭烘焙技巧分享',
          category: 'food',
          date: '2023-04-06',
          author: '美食生活',
          content: '掌握正确的面粉配比、发酵时间和烘烤温度是成功的关键。此外，选择优质的原料和保持厨房清洁也对成品质量有很大影响。通过不断练习，每个人都能成为烘焙高手。'
        }
      ]
    }
  },
  computed: {
    currentNews() {
      return this.newsItems.find(news => news.id == this.id)
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    getCategoryName(categoryId) {
      return this.categories[categoryId] || categoryId
    }
  },
  mounted() {
    // 详情页挂载后加载广告
    import('./ad.js').then(adModule => {
      adModule.initAdLoader();
    });
  }
}