import React, { Component } from "react";
import Navbar from "react-bootstrap/Navbar"

class HeaderBar extends Component {
    render() {
        return (
            <Navbar bg="dark" variant="dark">
              <Navbar.Brand href="#home">
                Github Visualiser
              </Navbar.Brand>
            </Navbar>
        );
    }
}

export default HeaderBar;