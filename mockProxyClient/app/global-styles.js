import { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  html,
  body {
    height: 100%;
    width: 100%;
  }

  body {
    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
    font-size: 14px;
    line-height: 1.42857143;
    color: #333;
    background-color: #fff;
  }

  .small-text {
    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
    font-size: 12px;
    line-height: 1.42857143;
    color: #333;
    background-color: #fff;
  }

  body.fontLoaded {
    font-family: 'Open Sans', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  }

  #app {
    background-color: #fafafa;
    min-height: 100%;
    min-width: 100%;
  }

  p,
  label {
    font-family: Georgia, Times, 'Times New Roman', serif;
    line-height: 1.5em;
  }

  .fixed-top-2 {
	margin-top: 56px;
}

.cursor-pointer {
	cursor: pointer
}

/** css styles for vertial tabs */
.tabs-left, .tabs-right {
	border-bottom: none;
	padding-top: 2px;
}

.tabs-left {
	border-right: 1px solid #ddd;
}

.tabs-right {
	border-left: 1px solid #ddd;
}

.tabs-left>li, .tabs-right>li {
	float: none;
	margin-bottom: 2px;
}

.tabs-left>li {
	margin-right: -1px;
}

.tabs-right>li {
	margin-left: -1px;
}

.tabs-left>li.active>a, .tabs-left>li.active>a:hover, .tabs-left>li.active>a:focus
	{
	border-bottom-color: #ddd;
	border-right-color: transparent;
}

.tabs-right>li.active>a, .tabs-right>li.active>a:hover, .tabs-right>li.active>a:focus
	{
	border-bottom: 1px solid #ddd;
	border-left-color: transparent;
}

.tabs-left>li>a {
	border-radius: 4px 0 0 4px;
	margin-right: 0;
	display: block;
}

.tabs-right>li>a {
	border-radius: 0 4px 4px 0;
	margin-right: 0;
}

/** css styles for slider */
.switch {
	position: relative;
	display: inline-block;
	width: 42px;
	height: 23px;
}

.switch input {
	opacity: 0;
	width: 0;
	height: 0;
}

.slider {
	position: absolute;
	cursor: pointer;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: #ccc;
	-webkit-transition: .4s;
	transition: .4s;
}

.slider:before {
	position: absolute;
	content: "";
	height: 15px;
	width: 16px;
	left: 4px;
	bottom: 4px;
	background-color: white;
	-webkit-transition: .4s;
	transition: .4s;
}

input:checked+.slider {
	background-color: #2196F3;
}

input:focus+.slider {
	box-shadow: 0 0 1px #2196F3;
}

input:checked+.slider:before {
	-webkit-transform: translateX(15px);
	-ms-transform: translateX(15px);
	transform: translateX(15px);
}

/* Rounded sliders */
.slider.round {
	border-radius: 34px;
}

.slider.round:before {
	border-radius: 50%;
}
`;

export default GlobalStyle;
