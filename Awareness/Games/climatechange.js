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
    question: "11% of all global greenhouse gas emissions caused by humans are due to deforestation.",
    answers: [
      {option:"True", answer:true},
      {option:"False", answer:false}
    ]
  },
  {
    question: "Greenland lost an average of 279 billion tons of ice per year between 1993 and 2019.",
    answers: [
      {option:"True", answer:true},
      {option:"False", answer:false}
    ]
  },
  {
    question: "The global sea level rose 4 inches in the last century.",
    answers: [
      {option:"True", answer:false},
      {option:"False", answer:true}
    ]
  },
  {
    question: "The acidity of the ocean has increased by about ___ since the beginning of the Industrial Revolution.",
    answers: [
      {option:"20%", answer:false},
      {option:"30%", answer:true}
    ]
  },
  {
    question: "The planet's average surface temperature has risen about 1 degree Farenheit since the late 19th century.",
    answers: [
      {option:"True", answer:false},
      {option:"False", answer:true}
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

