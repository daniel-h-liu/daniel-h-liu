var defaultThreads = [
  {
    id: 1,
    title: "The world is melting!",
    author: "Anon",
    date: Date.now(),
    content: "Thread content",
    comments: [
      {
        author: "SharonIsCaring",
        date: Date.now(),
        content: "Hey, everyone! Breaking news: https://www.worldwildlife.org/pages/why-are-glaciers-and-sea-ice-melting"
      },
      {
        author: "ABear",
        date: Date.now(),
        content: "Oh no! Thanks for the news and the link, Sharon!"
      }
    ]
  },
  {
    id: 2,
    title: "I think the world is in danger of hackers!!!",
    author: "Anon",
    date: Date.now(),
    content: "Thread content",
    comments: [
      {
        author: "Anon",
        date: Date.now(),
        content: "Is the pipeline about to burst?"
      },
      {
        author: "Anon",
        date: Date.now(),
        content: "no, but I think the packages are being compromised for shore"
      }
    ]
  }
]

var threads = defaultThreads
if(localStorage && localStorage.getItem('threads')) {
  threads = JSON.parse(localStorage.getItem('threads'));
  //localStorage.clear();
} else {
  threads = defaultThreads;
  localStorage.setItem('threads', JSON.stringify(defaultThreads));
}