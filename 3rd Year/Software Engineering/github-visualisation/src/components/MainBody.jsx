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

    updateBarChartData = (data) => {
        this.setState({
            barchartData: data
        })
    }
    render() {
        let barChart;
        if (this.state.barchartData === []){
            barChart = <div></div>;
        }
        else {
            console.log(this.state.barchartData);
            barChart = <BarChart data={this.state.barchartData}/>;
        }
        return (
            <Container>
                <Introduction/>
                <WelcomeForm dataFunction={this.updateBarChartData} />
                {barChart}
            </Container>
        );
    }

}

export default MainBody;