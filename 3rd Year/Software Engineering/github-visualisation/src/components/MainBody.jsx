import React, { Component } from "react";
import {Container} from "react-bootstrap"
import BarChart from "./BarChart";
import Introduction from "./Introduction"
import WelcomeForm from "./WelcomeForm"

class MainBody extends Component {
    constructor(props){
        super(props);
        this.state = {
            hasResult: false,
            barchartData: []
        }
    }
    render() {
        return (
            <Container>
                <Introduction/>
                <WelcomeForm data={this.state.data}/>
                <BarChart data={this.state.barchartData}/>
            </Container>
        );
    }

}

export default MainBody;