import React, { useState, useMemo } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme, PaletteMode } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import Navbar from './components/Navbar';
import EmployeePage from './pages/employee/EmployeePage';
import FindVerification from './pages/client/FindVerification';
import VerificationProcess from './pages/client/verification/VerificationProcess';
import VerificationDetails from './pages/employee/VerificationDetails';

function App() {
  const defaultTheme = (process.env.REACT_APP_DEFAULT_THEME as PaletteMode) || 'light';
  const [mode, setMode] = useState<PaletteMode>(defaultTheme);

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode,
          primary: {
            main: '#e2007b', // Magenta
          },
          secondary: {
            main: '#001fe6', // Ciemniejsza Magenta
          },
        },
        components: {
          MuiPaper: {
            styleOverrides: {
              root: {
                ...(mode === 'light' && {
                  boxShadow: '0px 3px 15px rgba(0,0,0,0.3)',
                }),
              },
            },
          },
          MuiInputBase: {
            styleOverrides: {
              input: {
                ...(mode === 'light' && {
                  '&.Mui-disabled': {
                    color: 'rgba(0, 0, 0, 0.9)',
                    WebkitTextFillColor: 'rgba(0, 0, 0, 0.7)',
                  },
                }),
              },
            },
          },
          MuiFormLabel: {
            styleOverrides: {
              root: {
                ...(mode === 'light' && {
                  '&.Mui-disabled': {
                    color: 'rgba(0, 0, 0, 0.8)',
                  },
                }),
              },
            },
          },
        },
      }),
    [mode]
  );

  const toggleColorMode = () => {
    setMode((prevMode) => (prevMode === 'light' ? 'dark' : 'light'));
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Navbar mode={mode} toggleColorMode={toggleColorMode} />
        <Routes>
          <Route path="/client" element={<FindVerification />} />
          <Route path="/client/verification/:id" element={<VerificationProcess />} />
          <Route path="/employee" element={<EmployeePage />} />
          <Route path="/employee/verification/:id" element={<VerificationDetails />} />
          <Route path="/" element={<Navigate to="/client" replace />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
