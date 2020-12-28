
import 'bootstrap/dist/css/bootstrap.min.css';
import HeaderBar from "./components/HeaderBar"
import MainBody from "./components/MainBody"
import './App.css';

function App() {
  document.title = "Visualisation";
  return (
        <div className="main">
            <HeaderBar />
            <MainBody />
        </div>
  );
}



export default App;
