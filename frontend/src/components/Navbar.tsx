import React, { FC } from 'react';
import { AppBar, Toolbar, Typography, Button, IconButton, Box, Divider, PaletteMode } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';
import { PL, US } from 'country-flag-icons/react/3x2';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';

interface NavbarProps {
  mode: PaletteMode;
  toggleColorMode: () => void;
}

const Navbar: FC<NavbarProps> = ({ mode, toggleColorMode }) => {
  const { t, i18n } = useTranslation();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

  const handleLanguageMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const changeLanguage = (lng: string) => {
    i18n.changeLanguage(lng);
    handleClose();
  };

  return (
    <AppBar position="static">
      <Toolbar sx={{ minHeight: '64px', p: 0 }}>
        <Typography
          variant="h6"
          component={RouterLink}
          to="/"
          sx={{
            textDecoration: 'none',
            color: 'white',
            bgcolor: '#e2007b',
            px: 2,
            height: '64px',
            display: 'flex',
            alignItems: 'center',
            fontWeight: 'bold'
          }}
        >
          PowerScore
        </Typography>
        {mode === 'light' && <Divider orientation="vertical" flexItem sx={{ bgcolor: 'rgba(255, 255, 255, 0.5)', width: '2px' }} />}
        <Box sx={{ display: 'flex', alignItems: 'center', px: 2 }}>
          <Button color="inherit" component={RouterLink} to="/client">
            {t('navbar.client')}
          </Button>
          <Button color="inherit" component={RouterLink} to="/employee" sx={{ ml: 1 }}>
            {t('navbar.employee')}
          </Button>
        </Box>
        <Box sx={{ flexGrow: 1 }} />
        <Box sx={{ display: 'flex', alignItems: 'center', px: 2 }}>
          <IconButton
            sx={{
              ml: 1,
              filter: 'drop-shadow(0 0 5px rgba(0, 0, 0, 0.8))'
            }}
            onClick={handleLanguageMenu}
            color="inherit"
          >
            {i18n.language.startsWith('pl') ? <PL title="Polski" style={{ width: '24px' }} /> : <US title="English" style={{ width: '24px' }} />}
          </IconButton>
          <Menu
            anchorEl={anchorEl}
            open={Boolean(anchorEl)}
            onClose={handleClose}
          >
            <MenuItem onClick={() => changeLanguage('pl')} sx={{ gap: 1 }}>
              <PL title="Polski" style={{ width: '24px' }} />
            </MenuItem>
            <MenuItem onClick={() => changeLanguage('en')} sx={{ gap: 1 }}>
              <US title="English" style={{ width: '24px' }} />
            </MenuItem>
          </Menu>
          <IconButton sx={{ ml: 1 }} onClick={toggleColorMode} color="inherit">
            {mode === 'dark' ? <Brightness7Icon /> : <Brightness4Icon />}
          </IconButton>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
