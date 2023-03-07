const restartBtn = document.getElementById("restart");
const prevBtn = document.getElementById("prev");
const nextBtn = document.getElementById("next");
const submitBtn = document.getElementById("submit");
const trueBtn = document.getElementById("True");
const falseBtn = document.getElementById("False");
const userScore = document.getElementById("user-score");
const questionText = document.getElementById("question-text");

let currentQuestion = 0;
var score = 0;
let questions = [
  {
    question: "50 countries have have legalized same-sex marriage.",
    answers: [
      {option:"True", answer:false},
      {option:"False", answer:true}
    ]
  },
  {
    question: "The creator of the rainbow flag was ____",
    answers: [
      {option:"Gilbert Baker", answer:true},
      {option:"Ruth Hunt", answer:false}
    ]
  },
  {
    question: "Pride Month is held in June to commemorate the Stonewall riots.",
    answers: [
      {option:"True", answer:true},
      {option:"False", answer:false}
    ]
  },
  {
    question: "Pride Month has been officially reocgnized by ___ US presidents.",
    answers: [
      {option:"3", answer:true},
      {option:"5", answer:false}
    ]
  },
  {
    question: "Which South American country has held the biggest Pride Parade to date?",
    answers: [
      {option:"Argentina", answer:false},
      {option:"Brazil", answer:true}
    ]
  }
]

restartBtn.addEventListener('click', restart);
prevBtn.addEventListener('click', prev);
nextBtn.addEventListener('click', next);
submitBtn.addEventListener('click', submit);


function beginQuiz(){
  currentQuestion = 0;
  questionText.innerHTML = questions[currentQuestion].question;
  trueBtn.innerHTML = questions[currentQuestion].answers[0].option;
  trueBtn.onclick = () => {
    let ano = 0;
    if(questions[currentQuestion].answers[ano].answer){
      if(score < 5){
        score++;
      }
    }
    userScore.innerHTML = score;
    if(currentQuestion < 4){
      next();
    }
  }
  falseBtn.innerHTML = questions[currentQuestion].answers[1].option;
  falseBtn.onclick = () => {
    let ano = 1;
    if(questions[currentQuestion].answers[ano].answer){
      if(score < 5){
        score++;
      }
    }
    userScore.innerHTML = score;
    if(currentQuestion < 4){
      next();
    }
  }
  prevBtn.classList.add('hide');
}

function restart(){
  currentQuestion = 0;
  prevBtn.classList.remove('hide');
  nextBtn.classList.remove('hide');
  submitBtn.classList.remove('hide');
  trueBtn.classList.remove('hide');
  falseBtn.classList.remove('hide');
  score = 0;
  userScore.innerHTML = score;
  beginQuiz();
}

function next(){
  currentQuestion++;
  if(currentQuestion >= 4){
    nextBtn.classList.add('hide');
    prevBtn.classList.remove('hide');
  }
  questionText.innerHTML = questions[currentQuestion].question;
  trueBtn.innerHTML = questions[currentQuestion].answers[0].option;
  trueBtn.onclick = () => {
    let ano = 0;
    if(questions[currentQuestion].answers[ano].answer){
      if(score < 5){
        score++;
      }
    }
    userScore.innerHTML = score;
    if(currentQuestion < 4){
      next();
    }
  }
  falseBtn.innerHTML = questions[currentQuestion].answers[1].option;
  falseBtn.onclick = () => {
    let ano = 1;
    if(questions[currentQuestion].answers[ano].answer){
      if(score < 5){
        score++;
      }
    }
    userScore.innerHTML = score;
    if(currentQuestion < 4){
      next();
    }
  }
  prevBtn.classList.remove('hide');
}

function prev(){
  currentQuestion--;
  if(currentQuestion <= 0){
    prevBtn.classList.add('hide');
    nextBtn.classList.remove('hide');
  }
  questionText.innerHTML = questions[currentQuestion].question;
  trueBtn.innerHTML = questions[currentQuestion].answers[0].option;
  trueBtn.onclick = () => {
    let ano = 0;
    if(questions[currentQuestion].answers[ano].answer){
      if(score < 5){
        score++;
      }
    }
    userScore.innerHTML = score;
    if(currentQuestion < 4){
      next();
    }
  }
  falseBtn.innerHTML = questions[currentQuestion].answers[1].option;
  falseBtn.onclick = () => {
    let ano = 1;
    if(questions[currentQuestion].answers[ano].answer){
      if(score < 5){
        score++;
      }
    }
    userScore.innerHTML = score;
    if(currentQuestion < 4){
      next();
    }
  }
  nextBtn.classList.remove('hide');
}

function submit(){
  prevBtn.classList.add('hide');
  nextBtn.classList.add('hide');
  submitBtn.classList.add('hide');
  trueBtn.classList.add('hide');
  falseBtn.classList.add('hide');
  questionText.innerHTML = "Congratulations on completing the quiz!"
}

console.log("hey!");
beginQuiz();

